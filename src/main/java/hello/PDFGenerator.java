package hello;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.mysql.cj.jdbc.MysqlDataSource;
import twitter4j.JSONArray;
import twitter4j.JSONObject;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class PDFGenerator {

    public String generatePdf(int reportid){
        Connection connect = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            
            //get report data from database using reportid
            MysqlDataSource dataSource = new MysqlDataSource();
            dataSource.setUser("tcsproject");
            dataSource.setPassword("secret");
            dataSource.setServerName("192.168.2.200");
            dataSource.setDatabaseName("tcsproject");

            connect = dataSource.getConnection();

            preparedStatement = connect
                    .prepareStatement("SELECT searchParams, searchResults from tcsproject.twitterSearchReports WHERE id = ?");
            preparedStatement.setLong(1, reportid);
            resultSet = preparedStatement.executeQuery();
            resultSet.first();
            String params = resultSet.getString(1);
            String data = resultSet.getString(2);

            //generate pdf report
            //Initialize PDF writer
            PdfWriter writer = new PdfWriter("report_"+reportid+".pdf");

            //Initialize PDF document
            PdfDocument pdf = new PdfDocument(writer);

            // Initialize document
            Document document = new Document(pdf);

            document.add(new Paragraph("Search Parameters: " + params+"\n\n"));

            JSONArray jsa = new JSONArray(data);
            for(int c=0;c<jsa.length();c++){
                JSONObject jso = jsa.getJSONObject(c);
                document.add(new Paragraph(jso.getString("name") + ": "+ jso.getString("tweet")));
            }

            //Close document
            document.close();
        }catch(Exception e){
            e.printStackTrace();
        }finally {
            try {
                if (resultSet != null)
                    resultSet.close();

                if (connect != null)
                    connect.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return "report_"+reportid+".pdf";
    }
}
