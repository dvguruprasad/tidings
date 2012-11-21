require 'csv'

require File.expand_path(File.join(File.dirname(__FILE__), "db"))
require File.expand_path(File.join(File.dirname(__FILE__), "tweet"))
require File.expand_path(File.join(File.dirname(__FILE__), "test_data"))

class Populator
    def populate(file)
        Tweet.delete_all
        TestData.delete_all

        id = 1
        CSV.foreach(file) do |line|
            create_tweet_and_test_data(id, line)
            id += 1
        end
    end

    def create_tweet_and_test_data(id, parts)
        tweet = Tweet.new(:identifier => id, :category => sentiment(parts[0]), :fullText => parts[1])
        tweet.save

        test_data = TestData.new(:identifier => id, :fullText => parts[1])
        test_data.save
    end

    def sentiment(number)
        number == "0" ? "negative" : "positive"
    end
end

Populator.new.populate(ARGV[0])
