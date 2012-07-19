class TrainingController < ApplicationController
  def index
    @training_data = TrainingData.where(:categorized => [nil, false]).all
    @categories = Category.all
  end

  def classify
    @news_item_id = params[:news_item_id]
    @category = params[:category]

    TrainingData.classify(@news_item_id, @category)
    redirect_to :action => :index
  end
end
