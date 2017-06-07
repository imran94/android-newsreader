package com.imran.mynewsreader;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.LayoutRes;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 16-Jan-17.
 */
public class ArticleAdapter extends ArrayAdapter<Article> {
    private List<Article> articles;

    public ArticleAdapter(Context context, @LayoutRes int resource, List<Article> objects) {
        super(context, resource, objects);

        articles = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Article currentArticle = articles.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.link_view, null);
        }

        //Fill the view
        TextView title = (TextView) convertView.findViewById(R.id.title);
        title.setText(currentArticle.getTitle());

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(currentArticle.getUrl()));
                getContext().startActivity(browserIntent);
            }
        });

        return convertView;
    }
}
