class NewsController < ApplicationController
  def index
    @news_items = NewsItem.order(:published_date).page params[:page]
  end
end
