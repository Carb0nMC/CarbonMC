package io.github.carbon.paper.npc.utils;

import com.google.gson.Gson;
import org.apache.maven.artifact.versioning.ComparableVersion;

import java.io.IOException;
import java.net.URL;
import java.util.Map;

public class VersionFetcher {

    public static final String DOWNLOAD_URL = "https://modrinth.com/plugin/npc-plugin/versions";
    private static final String API_URL = "https://api.modrinth.com/v2/project/npc-plugin/version";
    private static ComparableVersion newestVersion = null;

    public static ComparableVersion getNewestVersion(){
        return newestVersion != null ? newestVersion : fetch(API_URL);
    }

    /**
     * @return the newest version string
     */
    private static ComparableVersion fetch(String url){
        String jsonString = null;
        try {
            jsonString = getDataFromUrl(url);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Parse the JSON data into a Map
        Gson gson = new Gson();
        Map<String, Object>[] versions = gson.fromJson(jsonString, Map[].class);

        // Get the first version in the list
        Map<String, Object> firstVersion = versions[0];
        String versionNumber = (String) firstVersion.get("version_number");

        ComparableVersion ver = new ComparableVersion(versionNumber);

        newestVersion = ver;
        return ver;
    }

    private static String getDataFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        try (java.util.Scanner scanner = new java.util.Scanner(url.openStream(), "UTF-8").useDelimiter("\\A")) {
            return scanner.hasNext() ? scanner.next() : "";
        }
    }
}
