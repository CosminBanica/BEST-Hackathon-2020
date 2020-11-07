package com.covid.codelorians.services;

import com.covid.codelorians.models.CovidArticle;
import com.covid.codelorians.models.Tweet;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.ws.transport.http.HttpUrlConnection;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Stream;

@Service
public class TweetStreamService {
    private static String DATA_URL = "https://api.twitter.com/2/tweets/search/stream?expansions=author_id&user.fields=profile_image_url";
    private InputStream inputStream;
    private Queue<Tweet> tweets = new LinkedList<>();
    Scanner scanner;

    @PostConstruct
    @Scheduled(cron = "* * 1 * * *")
    public void fetchData() throws IOException, InterruptedException {
        List<Tweet> newTweets = new ArrayList<>();
        HttpClient client = HttpClient.newHttpClient();
        System.out.println("sal");

        HttpURLConnection connection = (HttpURLConnection) new URL(DATA_URL).openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer AAAAAAAAAAAAAAAAAAAAAHAUJgEAAAAADcGAP6w0a%2B%2B1Sn4cRyYZaSi7%2FdQ%3DuXZqJZMUb2HXx51NIhnWp7yGD0wenYnyWs7PN0x2aWglImQJXK");

        inputStream = connection.getInputStream();
        scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
    }

    public void fillList() {
        String tweetData = "0";
        tweetData = scanner.useDelimiter("}\r\n").next() + "}";
        int addedTweets = 0;

        while (tweetData.startsWith("{") || addedTweets < 10) {
            addedTweets++;
            tweetData = scanner.useDelimiter("}\r\n").next() + "}";

            JSONObject tweetJson = new JSONObject(tweetData);

            if (tweets.size() < 10) {
                tweets.add(new Tweet(tweetJson));
            } else {
                tweets.remove();
                tweets.add(new Tweet(tweetJson));
            }
        }
    }

    public Queue<Tweet> getTweets() {
        return tweets;
    }
}
