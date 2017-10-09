package info706.zikub.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import info706.zikub.R;
import info706.zikub.models.Music;

public class MusicAdapter extends ArrayAdapter {
    private List<Music> musics;

    public MusicAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
        musics = objects;
    }

    @Override
    public int getCount() {
        return musics.size();
    }

    @Override
    public Object getItem(int position) {
        return musics.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.music_item, null);
        }

        TextView title = (TextView)convertView.findViewById(R.id.title);
        TextView author = (TextView)convertView.findViewById(R.id.author);

        title.setText(musics.get(position).getTitle());
        author.setText(musics.get(position).getAuthor());

        return convertView;
    }
}
