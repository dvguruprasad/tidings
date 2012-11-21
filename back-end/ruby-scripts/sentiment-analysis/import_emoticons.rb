require File.expand_path(File.join(File.dirname(__FILE__), "db"))
require 'csv'

class Score
    include MongoMapper::EmbeddedDocument
    key :frequency, Integer 
    key :probability, Float
end

class CategoryScores
    include MongoMapper::EmbeddedDocument
    key :positive, :class => Score 
    key :negative, :class => Score 

    def self.create(sentiment_score)
        scores = CategoryScores.new
        sentiment_score = sentiment_score.to_f
        if(sentiment_score > 0)
            scores.positive = Score.new(:frequency => 0, :probability => sentiment_score.abs)
            scores.negative = Score.new(:frequency => 0, :probability => 0)
        else
            scores.positive = Score.new(:frequency => 0, :probability => 0)
            scores.negative = Score.new(:frequency => 0, :probability => sentiment_score.abs)
        end
    end
end

class CategoryDistribution
    include MongoMapper::Document
    set_collection_name "tweets_category_distributions"
    key :word, String
    key :categoryScores, :class => CategoryScores
end

def category_scores(sentiment_score)
    { 
        :negative => {:frequency => 0, :probability => sentiment_score < 0 ? sentiment_score.abs : 0},
        :positive => {:frequency => 0, :probability => sentiment_score > 0 ? sentiment_score : 0}
    }
end

CSV.foreach(File.expand_path(File.join(File.dirname(__FILE__), "../../data/emoticons.csv"))) do |line|
    CategoryDistribution.create(:word => line[1], :categoryScores => category_scores(line[0].to_f))
end

