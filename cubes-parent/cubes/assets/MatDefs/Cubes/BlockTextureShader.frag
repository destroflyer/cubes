void main(){
    float epsilon = 0.00001;
    vec3 loc = vec3(int((vertPosition[0] / 3.0) + epsilon), int((vertPosition[1] / 3.0) + epsilon), int((vertPosition[2] / 3.0) + epsilon));
    vec3 off = vec3(mod(vertPosition[0] + epsilon, 3.0), mod(vertPosition[1] + epsilon, 3.0), mod(vertPosition[2] + epsilon, 3.0)) / 3.0;
    vec2 texcoordOff = vec2(0,0);
    if(off.x <= epsilon){
        texcoordOff.x = ((vertNormal.x < -0.5)?off.z:(1.0 - off.z));
        texcoordOff.y = off.y;
    }
    else if(off.y <= epsilon){
        texcoordOff.x = ((vertNormal.y > 0.5)?off.z:(1.0 - off.z));
        texcoordOff.y = off.x;
    }
    else if(off.z <= epsilon){
        texcoordOff.x = ((vertNormal.z > 0.5)?off.x:(1.0 - off.x));
        texcoordOff.y = off.y;
    }
    vec2 texcoord = vec2(tileCoord.x + texcoordOff.x, tileCoord.y + (1.0 - texcoordOff.y)) / 16.0;
    texcoord.y = (1.0 - texcoord.y);
    vec4 color = texture2D(m_BlockMap, texcoord);
    outColor = color;
}