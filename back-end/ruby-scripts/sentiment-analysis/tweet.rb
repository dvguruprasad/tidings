class Tweet
    include MongoMapper::Document
    set_collection_name "tweets_to_test_against"
    key :identifier, Integer
    key :fullText, String
    key :category, String
end
