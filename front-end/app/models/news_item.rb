class NewsItem
  include MongoMapper::Document
  key :title, String
  key :transformed_content, String
  key :link, String
  key :published_date, Date
end
