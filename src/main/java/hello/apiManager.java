package hello;

import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
public class apiManager {
    /*
        possible improvements:
        - add a threadpool to handles search requests
        - write pdf directly to output stream
        - get more pages of results from twitter
        - statistics on tweets in pdf report
        - db java object to hide the common db code in a tightly coupled object
    
    */

    @RequestMapping(value="/generatereport", method = RequestMethod.POST)
    public ResponseEntity<Void> startReport(@RequestBody(required=false) String searchParam){

        //parse search params from json string
        TwitterSearchParams searchParams = new TwitterSearchParams(searchParam);

        ReportGenerator rg = new ReportGenerator(searchParams);
        
        //makes and saves report in database
        long reportID = rg.getSearchResults();

        HttpHeaders headers = new HttpHeaders();
        headers.add("reportid",((Long)reportID).toString());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
    }

    //not used unless async searching is implemented
    @RequestMapping(value="/reportstatus", method = RequestMethod.POST)
    public ResponseEntity<Void> reportStatus(@RequestParam("reportID") String reportID){

        return new ResponseEntity<Void>(HttpStatus.CONFLICT);
    }

    @RequestMapping(value="/getreport")
    public void getReport(@RequestParam String reportID, HttpServletResponse response){
        PDFGenerator pdf = new PDFGenerator();
        File pdfFile = new File(pdf.generatePdf(Integer.parseInt(reportID)));
        try {
            // get your file as InputStream
            InputStream is = new FileInputStream(pdfFile);
            // copy it to response's OutputStream
            FileCopyUtils.copy(is,response.getOutputStream());
            response.flushBuffer();
            response.setContentType("application/pdf");
        } catch (IOException ex) {
            throw new RuntimeException("IOError writing file to output stream");
        }
    }
}
