package com.ntropia.filmico.utilities;

import java.util.Map;

public class UrlBulder {

    public static String generateUrlAddress(String apiURL, String apiKey, String apiResource, Map<String, String> optionalParams) {
        return generateUrlAddress(apiURL, apiKey, apiResource, 0, "", optionalParams);
    }

    public static String generateUrlAddress(String apiURL, String apiKey, String apiResource,
                                            int entityId, String additionalResource,
                                            Map<String, String> optionalParams) {
        StringBuilder sb = new StringBuilder();

        sb.append(apiURL);
        sb.append(apiResource);

        if (entityId != 0) sb.append("/" + entityId);
        if (!additionalResource.isEmpty()) sb.append("/" + additionalResource);

        sb.append("?");
        sb.append("api_key=" + apiKey);
        if (optionalParams != null) {
            for (Map.Entry<String, String> entry : optionalParams.entrySet()) {
                sb.append(entry.getKey() + "=" + entry.getValue());
            }
        }

        return sb.toString();
    }

    public static String generatePosterImageUrl(String imageName, boolean isPoster) {
        String host = "https://image.tmdb.org/t/p/";
        String posterFormat = "w300_and_h450_bestv2";
        String backdropFormat = "w500_and_h281_bestv2";

        StringBuilder sb = new StringBuilder();
        sb.append(host);
        if (isPoster)
            sb.append(posterFormat);
        else
            sb.append(backdropFormat);

        sb.append(imageName);

        return sb.toString();
    }

}
