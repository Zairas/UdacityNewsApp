package com.example.myappnews.utils;

import android.text.TextUtils;
import android.util.Log;

import com.example.myappnews.model.Article;

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
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = QueryUtils.class.getName();

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    /**
     * Busca o dataset USGS e retorna uma lista de objetos {@link Article}.
     */
    public static List<Article> fetchArticleData(String requestUrl) {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Cria objeto URL
        URL url = createUrl(requestUrl);

        // Realiza requisição HTTP para a URL e recebe uma resposta JSON de volta
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem making the HTTP request.", e);
        }

        // Extrai campos relevantes da resposta JSON e cria uma lista de {@link Earthquake}s
        List<Article> articles = extractFeatureFromJson(jsonResponse);

        // Retorna a lista de {@link Earthquake}s
        return articles;
    }

    /**
     * Retorna o novo objeto URL da dada URL de string.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Problem building the URL ", e);
        }
        return url;
    }

    /**
     * Realiza uma requisição HTTP para a dada URL e retorna uma String como resposta.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Se a requisição foi bem sucedida (código de resposta 200),
            // então lê a entrada de dados e decodifica a resposta.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the article JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                // Fechar a entrada de dados pode acarretar IOException, e é por isto que
                // a assinatura do método makeHttpRequest(URL url) especifica um IOException
                // que pode ser causado.
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Converte a {@link InputStream} em uma String que contém
     * toda a resposta JSON do servidor.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Retorna uma lista de objetos {@link Article} que foram obtidos da
     * decodificação da dada resposta JSON.
     */
    private static List<Article> extractFeatureFromJson(String articleJSON) {
        // Se a String do JSON é vazia ou nula, então retorna agora.
        if (TextUtils.isEmpty(articleJSON)) {
            return null;
        }

        // Cria um ArrayList vazio para o qual podemos iniciar adicionando objetos Earthquake.
        List<Article> articles = new ArrayList<>();

        // Tenta decodificar a string de resposta da JSON. Se houver um problema no modo como o JSON
        // é formatado, uma exceção de objeto JSONException será chamada.
        // Ao pegar a exceção no Catch o aplicativo não quebra, e imprime a mensagem de erro para os logs.
        try {

            // Cria um objeto JSONObject da string de resposta JSON
            JSONObject baseJsonResponse = new JSONObject(articleJSON);

            JSONObject jsonObject = baseJsonResponse.getJSONObject("response");

            JSONArray articlesArray = jsonObject.getJSONArray("results");
            // Para cada earthquake no earthquakeArray, cria um objeto {@link Earthquake}
            for (int i = 0; i < articlesArray.length(); i++) {

                // Obtém um único earthquake na posição i dentro da lista de earthquakes
                JSONObject currentArticle = articlesArray.getJSONObject(i);

                // Extrai o valor da chave chamada "sectionId"
                String sectionId = currentArticle.getString("sectionId");

                // Extrai o valor da chave chamada "sectionName"
                String sectionName = currentArticle.getString("sectionName");

                // Extrai o valor da chave chamada "webPublicationDate"
                String webPublicationDate = currentArticle.getString("webPublicationDate");

                // Extrai o valor da chave chamada "webTitle"
                String webTitle = currentArticle.getString("webTitle");

                // Extrai o valor da chave chamada "url"
                String url = currentArticle.getString("webUrl");

                // Cria um novo objeto {@link Article} com a sectionId, sectionName, webPublicationDate, webTitle
                // e url da resposta JSON.
                Article article = new Article(sectionId, sectionName, webPublicationDate, webTitle, url);

                // Adiciona o novo {@link Earthquake} a lista de earthquakes.
                articles.add(article);
            }

        } catch (JSONException e) {
            // Se um erro é acionado ao executar qualquer um dos comando no bloco "try",
            // processa o catch da exceção aqui, para que o aplicativo não quebre. Imprime a mensagem log
            // com a mensagem da exceção.
            Log.e("QueryUtils", "Problem parsing the article JSON results", e);
        }

        // Retorna a lista de earthquakes
        return articles;
    }

}
