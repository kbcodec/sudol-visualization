package com.sudol.SudolAPI.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public class ExternalApiResponse {
    @JsonProperty("channel")
    private ChannelModel channel;
    @JsonProperty("feeds")
    private List<FeedModel> feeds;

    public ChannelModel getChannel() {
        return channel;
    }

    public void setChannel(ChannelModel channel) {
        this.channel = channel;
    }

    public List<FeedModel> getFeeds() {
        return feeds;
    }

    public void setFeeds(List<FeedModel> feeds) {
        this.feeds = feeds;
    }
}
