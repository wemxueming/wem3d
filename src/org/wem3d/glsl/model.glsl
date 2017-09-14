const float PI = 3.14159265359;

vec3 ParallaxMapping(sampler2D depthMap, vec2 texCoords, vec2 wrap, vec3 viewDir, float heightScale)
{
    // number of depth layers
    const float minLayers = 10;
    const float maxLayers = 15;
    float numLayers = mix(maxLayers, minLayers, abs(dot(vec3(0.0, 0.0, 1.0), viewDir)));
    // calculate the size of each layer
    float layerDepth = 1.0 / numLayers;
    // depth of current layer
    float currentLayerDepth = 0.0;
    // the amount to shift the texture coordinates per layer (from vector P)
    vec2 P = viewDir.xy / viewDir.z * heightScale;
    vec2 deltaTexCoords = P / numLayers;
    // get initial values
    vec2  currentTexCoords     = texCoords;
    float currentDepthMapValue = texture(depthMap, currentTexCoords * wrap).r;
    while(currentLayerDepth < currentDepthMapValue)
    {
        // shift texture coordinates along direction of P
        currentTexCoords -= deltaTexCoords;
        // get depthmap value at current texture coordinates
        currentDepthMapValue = texture(depthMap, currentTexCoords * wrap).r;
        // get depth of next layer
        currentLayerDepth += layerDepth;
    }
    // get texture coordinates before collision (reverse operations)
    vec2 prevTexCoords = currentTexCoords + deltaTexCoords;
    // get depth after and before collision for linear interpolation
    float afterDepth  = currentDepthMapValue - currentLayerDepth;
    float beforeDepth = texture(depthMap, prevTexCoords * wrap).r - currentLayerDepth + layerDepth;
    // interpolation of texture coordinates
    float weight = afterDepth / (afterDepth - beforeDepth);
    vec2 finalTexCoords = prevTexCoords * weight + currentTexCoords * (1.0 - weight);

    float parallaxHeight = currentLayerDepth + beforeDepth * weight + afterDepth * (1.0 - weight);
    return vec3(finalTexCoords.x, finalTexCoords.y, parallaxHeight);
}


Material createMaterial(MaterialInfo info, sampler2D albedoMap, sampler2D metallicMap, sampler2D roughnessMap, sampler2D aoMap, sampler2D normalMap, sampler2D heightMap, vec2 texcoord, vec3 N, vec3 V, mat3 TBN, mat3 NBT)
{
    vec3 albedo = info.albedo;
    float metallic = info.metallic;
    float roughness = info.roughness;
    float ao = info.ao;
    vec3 normal = N;
    vec2 coord = texcoord;
    float height = -1;
    float heightScale = info.heightScale;
    vec2 wrap = info.wrap;

    if(info.isHeightMap)
    {
        vec3 parallax = ParallaxMapping(heightMap, coord , info.wrap, normalize(NBT * V), info.heightScale);
        coord = parallax.xy;
        height = parallax.z;
        if(texcoord.x > 1.0 || texcoord.y > 1.0 || texcoord.x < 0.0 || texcoord.y < 0.0)
        {
            discard;
        }
    }
    if(info.isNormalMap)
    {
        vec3 mapping = texture(normalMap, coord * info.wrap).xyz;
        mapping = mapping * 255./127. - 128./127.;
        normal = normalize(TBN * mapping);
    }

    if(info.isAlbedoMap)
    {
        albedo = pow(texture(albedoMap, coord * info.wrap).rgb, vec3(2.2));
    }
    if(info.isMetallicMap)
    {
        metallic = texture(metallicMap, coord * info.wrap).r;
    }
    if(info.isRoughnessMap)
    {
        roughness = texture(roughnessMap, coord * info.wrap).r;
    }
    if(info.isAoMap)
    {
        ao = texture(aoMap, coord * info.wrap).r;
    }

    Material m = Material(albedo, metallic, roughness, ao, normal, coord, height, heightScale, wrap);
    return m;
}

Ray createRay(LightInfo light, vec3 fragPos)
{
    vec3 radiance = light.color * light.brightness;
    vec3 direction;
    float attenuation = 1;

    if(light.attenuation.x == 0)
    {
        direction = normalize(-light.direction);
    }
    else
    {
        vec3 toLight = light.position - fragPos;
        direction = normalize(toLight);
        float distance = length(toLight);
        attenuation =  1.0 / (light.attenuation.x + light.attenuation.y * distance +light.attenuation.z * (distance * distance));
        if(light.cutoff != 0)
        {
            float theta = dot(direction, normalize(-light.direction));
            float epsilon = (light.cutoff - light.outcutoff);
            float intensity = clamp((theta - light.outcutoff) / epsilon, 0.0, 1.0);
            radiance *= intensity;
        }
    }
    Ray ray = Ray(radiance, direction, attenuation);
    return ray;
}

float distributionGGX(vec3 N, vec3 H, float roughness)
{
    float a      = roughness*roughness;
    float a2     = a*a;
    float NdotH  = max(dot(N, H), 0.0);
    float NdotH2 = NdotH*NdotH;

    float nom   = a2;
    float denom = (NdotH2 * (a2 - 1.0) + 1.0);
    denom = PI * denom * denom;

    return nom / denom;
}

float geometrySchlickGGX(float NdotV, float roughness)
{
    float r = (roughness + 1.0);
    float k = (r*r) / 8.0;

    float nom   = NdotV;
    float denom = NdotV * (1.0 - k) + k;

    return nom / denom;
}

float geometrySmith(vec3 N, vec3 V, vec3 L, float roughness)
{
    float NdotV = max(dot(N, V), 0.0);
    float NdotL = max(dot(N, L), 0.0);
    float ggx2  = geometrySchlickGGX(NdotV, roughness);
    float ggx1  = geometrySchlickGGX(NdotL, roughness);

    return ggx1 * ggx2;
}

vec3 fresnelSchlick(float cosTheta, vec3 F0)
{
    return F0 + (1.0 - F0) * pow(1.0 - cosTheta, 5.0);
}

float parallaxSoftShadowMultiplier(sampler2D heightMap, vec2 wrap, vec3 L, vec2 initialTexCoord,float initialHeight, float parallaxScale)
{
   float shadowMultiplier = 1;

   float minLayers = 15;
   float maxLayers = 30;

   // calculate lighting only for surface oriented to the light source
   if(dot(vec3(0, 0, 1), L) > 0)
   {
      // calculate initial parameters
      float numSamplesUnderSurface	= 0;
      shadowMultiplier	= 0;
      float numLayers	= mix(maxLayers, minLayers, abs(dot(vec3(0, 0, 1), L)));
      float layerHeight	= initialHeight / numLayers;
      vec2 texStep	= parallaxScale * L.xy / L.z / numLayers;

      // current parameters
      float currentLayerHeight	= initialHeight - layerHeight;
      vec2 currentTextureCoords	= initialTexCoord + texStep;
      float heightFromTexture	= texture(heightMap, currentTextureCoords * wrap).r;
      int stepIndex	= 1;


      // while point is below depth 0.0 )
      while(currentLayerHeight > 0 && layerHeight > 0)
      {
         // if point is under the surface
         if(heightFromTexture < currentLayerHeight)
         {
            // calculate partial shadowing factor
            numSamplesUnderSurface	+= 1;
            float newShadowMultiplier = (currentLayerHeight - heightFromTexture) *(1.0 - stepIndex / numLayers);
            shadowMultiplier = max(shadowMultiplier, newShadowMultiplier);
         }

         // offset to the next layer
         stepIndex	+= 1;
         currentLayerHeight	-= layerHeight;
         currentTextureCoords += texStep;
         heightFromTexture = texture(heightMap, currentTextureCoords * wrap).r;
      }

      // Shadowing factor should be 1 if there were no points under the surface
      if(numSamplesUnderSurface < 1)
      {
         shadowMultiplier = 1;
      }
      else
      {
         shadowMultiplier = 1.0 - shadowMultiplier;
      }
   }
   return shadowMultiplier;
}

vec4 brdf(Material m, Ray ray, vec3 V, mat3 NBT)
{
    vec3 albedo = m.albedo;
    float metallic = m.metallic;
    float roughness = m.roughness;
    float ao = m.ao;
    vec3 N = m.normal;
    vec3 R = ray.radiance;
    vec3 L = ray.direction;
    float attenuation = ray.attenuation;

        if(m.height != -1)
        {
            float shadow = parallaxSoftShadowMultiplier(heightMap, m.wrap, normalize(L * NBT), m.coord, m.height-0.05f, m.heightScale);
            //color = color * pow(shadow, 4);
            R = R * pow(shadow, 6);
        }

    vec3 F0 = vec3(0.04);
    F0 = mix(F0, albedo, metallic);
    // reflectance equation
    vec3 Lo = vec3(0.0);
    // calculate per-light radiance
    vec3 H = normalize(V + L);
    vec3 radiance     = R * attenuation;
    // cook-torrance brdf
    float NDF = distributionGGX(N, H, roughness);
    float G   = geometrySmith(N, V, L, roughness);
    vec3 F    = fresnelSchlick(max(dot(H, V), 0.0), F0);

    vec3 kS = F;
    vec3 kD = vec3(1.0) - kS;
    kD *= 1.0 - metallic;

    vec3 nominator    = NDF * G * F;
    float denominator = 4 * max(dot(N, V), 0.0) * max(dot(N, L), 0.0) + 0.001;
    vec3 specular     = nominator / denominator;

    // add to outgoing radiance Lo
    float NdotL = max(dot(N, L), 0.0);
    Lo += (kD * albedo / PI + specular) * radiance * NdotL;

    vec3 ambient = vec3(0.03) * albedo * ao;
    vec3 color = ambient + Lo;

    color = color / (color + vec3(1.0));
    color = pow(color, vec3(1.0/2.2));



    return vec4(color, 1);
}



