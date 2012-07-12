MongoMapper.connection = Mongo::Connection.new('localhost', 27017)
MongoMapper.database = "tidings_#{Rails.env}"
MongoMapper.connection.connect

# database_yaml = YAML::load(File.read(Rails.root.join('/config/database.yml')))
# if database_yaml[Rails.env] && database_yaml[Rails.env]['adapter'] == 'MongoDB'
#   mongo_database = database_yaml[Rails.env]
#   MongoMapper.connection = Mongo::Connection.new(mongo_database['host'], 27017, :pool_size => 5, :timeout => 5)
#   MongoMapper.database =  mongo_database['database']
#   MongoMapper.connection.connect
# end
