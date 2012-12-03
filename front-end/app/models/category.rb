class Category
  include MongoMapper::Document
  set_collection_name "news_categories"
  key :name, String
end
