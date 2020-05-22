package com.cleaningschedule.integrationtests;

import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.common.FileSource;
import com.github.tomakehurst.wiremock.extension.Parameters;
import com.github.tomakehurst.wiremock.extension.ResponseDefinitionTransformer;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.ResponseDefinition;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class DynamicTransformer extends ResponseDefinitionTransformer {
    private final String occupant = "Ghost Resident";

    @Override
    public ResponseDefinition transform(Request request, ResponseDefinition responseDefinition, FileSource files, Parameters parameters) {
        String[] path = request.getUrl().split("/");

        String response = "{\"buildingName\":\""+path[path.length-1]+"\","
                + "\"corridor\":\"F3L\",\"roomNumber\":"+path[path.length-2]+","
                + "\"occupantName\":\""+occupant+"\"}";

        return new ResponseDefinitionBuilder()
                .withHeader("Content-Type", "application/json")
                .withStatus(200)
                .withBody(response)
                .build();
    }

    @Override
    public String getName() {
        return "dynamic-transformer";
    }
}
