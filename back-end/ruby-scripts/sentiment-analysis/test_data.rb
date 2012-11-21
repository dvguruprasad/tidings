class TestData
    include MongoMapper::Document
    key :identifier, Integer
    key :fullText, String
end
