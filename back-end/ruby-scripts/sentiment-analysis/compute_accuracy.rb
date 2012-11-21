require File.expand_path(File.join(File.dirname(__FILE__), "db"))
require File.expand_path(File.join(File.dirname(__FILE__), "tweet"))
require File.expand_path(File.join(File.dirname(__FILE__), "news_item"))

class Accuracy
    def self.compute
        count = 0
        number_correctly_classified = 0

        original_tweets = Tweet.all
        Tweet.set_collection_name "tweets"

        original_tweets.each do |tweet|
            classified_tweet = Tweet.find_by_identifier(tweet.identifier)
            next if classified_tweet.nil?
            count += 1
            if classified_tweet.category == tweet.category
                number_correctly_classified += 1
            else 
                p "#{classified_tweet.category}: #{classified_tweet.fullText}"
            end
        end
        number_correctly_classified / count.to_f
    end
end

p "Accuracy: #{Accuracy.compute}"
