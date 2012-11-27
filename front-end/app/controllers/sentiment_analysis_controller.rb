class SentimentAnalysisController < ApplicationController
  def index
    @top_positive_tweets = Tweet.where(:category => "positive").limit(20).order(:score.desc)
    @top_negative_tweets = Tweet.where(:category => "negative").limit(20).order(:score.desc)
  end
end
