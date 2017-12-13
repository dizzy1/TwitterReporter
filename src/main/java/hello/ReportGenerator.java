package hello;

import com.mysql.cj.jdbc.MysqlDataSource;
import twitter4j.*;
import twitter4j.conf.ConfigurationBuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public class ReportGenerator {

    private final TwitterSearchParams search;

    public ReportGenerator(TwitterSearchParams search){
        this.search = search;
    }

    //TODO: do twitter search on a different thread and update the db record after completion
    //TODO: get more pages of search results
    public long getSearchResults() {
        Connection connect = null;
        PreparedStatement preparedStatement;
        ResultSet resultSet = null;

        int id = -1;

        try {
            ConfigurationBuilder cb = new ConfigurationBuilder();
            cb.setDebugEnabled(true)
                    .setOAuthConsumerKey("s")
                    .setOAuthConsumerSecret("s")
                    .setOAuthAccessToken("s")
                    .setOAuthAccessTokenSecret("s");

            TwitterFactory twitterf = new TwitterFactory(cb.build());
            Twitter twitter = twitterf.getInstance();
            Query query = new Query(search.toTwitterForm());

            //System.err.println(search.toTwitterForm());
            QueryResult result = twitter.search(query);

            JSONArray jsa = new JSONArray();

            //store tweets in json array with needed info
            for(Status s : result.getTweets()){
                JSONObject jso = new JSONObject();
                jso.put("name",s.getUser().getName());
                jso.put("tweet",s.getText());
                jsa.put(jso);
            }


            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("tcsproject");
            dataSource.setPassword("secret");
            dataSource.setServerName("192.168.2.200");
            dataSource.setDatabaseName("tcsproject");


            connect = dataSource.getConnection();

            //check for a duplicate search ( not accounting for timestamp )
            preparedStatement = connect
                    .prepareStatement("SELECT COUNT(*) FROM tcsproject.twitterSearchReports WHERE searchParams = ?");
            preparedStatement.setString(1, search.getBeforeParse()); // not best way
            resultSet = preparedStatement.executeQuery();

            resultSet.first();

            //if dup found, return its id
            if(resultSet.getInt(1) > 0){
                System.out.println("dup found");
                preparedStatement = connect
                        .prepareStatement("SELECT id from tcsproject.twitterSearchReports WHERE searchParams = ?");
                preparedStatement.setString(1, search.getBeforeParse()); // not best way
                resultSet = preparedStatement.executeQuery();

                resultSet.first();
                System.out.println("dup found "+resultSet.getInt(1));
                return resultSet.getInt(1);
            }


            //else do search and return new id
            preparedStatement = connect
                    .prepareStatement("insert into  tcsproject.twitterSearchReports values (default, ?, ?)");
            preparedStatement.setString(1, search.getBeforeParse());
            preparedStatement.setString(2, jsa.toString());
            preparedStatement.executeUpdate();

            //get new id
            preparedStatement = connect
                    .prepareStatement("SELECT id from tcsproject.twitterSearchReports WHERE searchParams = ?");
            preparedStatement.setString(1, search.getBeforeParse()); // not best way
            resultSet = preparedStatement.executeQuery();

            resultSet.first();
            id = resultSet.getInt(1);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                }

                if (connect != null) {
                    connect.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return id;
    }
}
