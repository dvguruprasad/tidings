class NewsController < ApplicationController
  def index
    if params[:category]
      @category = params[:category]
      @news_items = NewsItem.where(:category => params[:category].camelize).order(:published_date).page params[:page]
    else
      @news_items = NewsItem.order(:published_date).page params[:page]
    end
  end
end
