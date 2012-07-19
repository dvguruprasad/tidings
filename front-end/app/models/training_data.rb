class TrainingData
  include MongoMapper::Document
  key :title, String
  key :transformed_text, String
  key :link, String
  key :published_date, Date
  key :category, String
  key :categorized, Boolean,:default => false

  def TrainingData.classify(news_item_id, category)
    news_item = find_by_id(news_item_id)
    news_item.set({:category => category})
    news_item.set({:categorized => true})
  end
end
