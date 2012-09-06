class NewsController < ApplicationController
  def index
    @categories = Category.all
    if params[:category]
      @current_category = params[:category]
      @news_items = NewsItem.where(:category => params[:category]).order(:publishedDate.desc)
    else
      @news_items = NewsItem.order(:published_date)
    end
  end
end
