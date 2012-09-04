class NewsController < ApplicationController
  def index
    @categories = Category.all
    if params[:category]
      @current_category = params[:category]
      @news_items = NewsItem.where(:category => params[:category].camelize).order(:published_date).page params[:page]
    else
      @news_items = NewsItem.order(:published_date).page params[:page]
    end
  end
end
