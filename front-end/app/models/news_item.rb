class NewsItem
  include MongoMapper::Document
  key :title, String
  key :raw_content, String
  key :transformed_content, String
end
