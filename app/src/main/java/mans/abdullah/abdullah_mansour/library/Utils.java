package mans.abdullah.abdullah_mansour.library;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

public class Utils {

    public static final String LOG_TAG = Utils.class.getSimpleName();

    public static ArrayList<DataClass> fetchBooksData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an {@link Event} object
        ArrayList<DataClass> book = extractFeatureFromJson(jsonResponse);

        // Return the {@link Event}
        return book;
    }

    // Returns new URL object from the given string URL.
    private static URL createUrl(String stringUrl)
    {
        // declare new URL
        URL url = null;

        // surround with try and catch
        try
        {
            // initialize URL and set param to string to create URL
            url = new URL(stringUrl);
        } catch (MalformedURLException e)
        {
            e.printStackTrace();
        }
        // return URL
        return url;
    }

    // Make an HTTP request to the given URL and return a String as the response.
    private static String makeHttpRequest(URL url) throws IOException
    {
        // declare new String
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        // declare new HttpURLConnection
        HttpURLConnection urlConnection = null;
        // declare new InputStream
        InputStream inputStream = null;
        // surround with try and catch
        try
        {
            // create HTTP request
            urlConnection = (HttpURLConnection) url.openConnection();
            // set request method
            urlConnection.setRequestMethod("GET");
            // connect to server
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200)
            {
                // get InputStream with data to read it
                inputStream = urlConnection.getInputStream();
                // read from stream and return string then saved it in jsonResponse
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results.", e);
        }
        // at finally
        finally
        {
            // if urlConnection return with data
            if (urlConnection != null)
            {
                // then disconnect (disconnect don't mean delete data from urlConnection)
                urlConnection.disconnect();
            }
            // if inputStream return with data
            if (inputStream != null)
            {
                // then close (close don't mean delete data from inputStream)
                inputStream.close();
            }
        }
        // return jsonResponse with data
        return jsonResponse;
    }

    // readFromStream to the given InputStream then return String (JSON)
    private static String readFromStream(InputStream inputStream) throws IOException
    {
        // initialize StringBuilder
        StringBuilder stringBuilder = new StringBuilder();
        // if inputStream return with data
        if (inputStream != null)
        {
            // initialize InputStreamReader with two params (inputStream, Symbol rate)
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            // initialize BufferedReader with one param (InputStreamReader)
            BufferedReader reader = new BufferedReader(inputStreamReader);
            // new String to save data from BufferedReader line by line
            String line = reader.readLine();
            // loop to update String line by line and append this lines to stringBuilder
            while (line != null)
            {
                stringBuilder.append(line);
                line = reader.readLine();
            }
        }
        // return stringBuilder
        return stringBuilder.toString();
    }

    // extractFeatureFromJson to the given String then return ArrayList<DataClass>
    private static ArrayList <DataClass> extractFeatureFromJson(String booksJSON) {
        // If the JSON string is empty or null, then return early.
        if (TextUtils.isEmpty(booksJSON))
        {
            return null;
        }

        // Create an empty ArrayList that we can start adding books to
        ArrayList<DataClass> books = new ArrayList<>();
        // surround with try and catch
        try {
            // initialize baseJSONObject
            JSONObject baseJsonResponse = new JSONObject(booksJSON);
            // initialize JSONArray to get items array
            JSONArray itemsArray = baseJsonResponse.getJSONArray("items");
            // loop to get all items in itemsArray
            for (int i =0 ; i < itemsArray.length() ; i++)
            {
                // JSONObject to get data from each item
                JSONObject firstitem = itemsArray.getJSONObject(i);
                // JSONObject to get data from volumeInfo
                JSONObject info = firstitem.getJSONObject("volumeInfo");
                // String to get book title
                String title = info.getString("title");
                // JSONArray to get author name
                //JSONArray authorarray = info.getJSONArray("authors");
                // loop to get all items in authorarray

                String pup = info.getString("publisher");

                JSONObject imageurl = info.getJSONObject("imageLinks");

                String thumbnail = imageurl.getString("thumbnail");

                books.add(new DataClass(title,pup,thumbnail));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Problem parsing the earthquake JSON results", e);
        }
        // return Arraylist books
        return books;
    }
}
