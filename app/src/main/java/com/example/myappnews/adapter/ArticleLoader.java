package com.example.myappnews.adapter;

import android.content.Context;

import androidx.loader.content.AsyncTaskLoader;

import com.example.myappnews.model.Article;
import com.example.myappnews.utils.QueryUtils;

import java.util.List;

public class ArticleLoader extends AsyncTaskLoader<List<Article>> {

    /** Tag para mensagens de log */
    private static final String LOG_TAG = ArticleLoader.class.getName();

    /** URL da busca */
    private String mUrl;

    /**
     * Constrói um novo {@link ArticleLoader}.
     *
     * @param context O contexto da activity
     * @param url A URL de onde carregaremos dados
     */
    public ArticleLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     * Está e uma thread de background.
     */
    @Override
    public List<Article> loadInBackground() {
        if (mUrl == null) {
            return null;
        }

        // Realiza requisição de rede, decodifica a resposta, e extrai uma lista de earthquakes.
        List<Article> articles = QueryUtils.fetchArticleData(mUrl);
        return articles;
    }
}
