package info706.zikub.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import info706.zikub.R;
import info706.zikub.models.Music;

public class MusicEditionAdapter extends ArrayAdapter {
    private List<Music> musics;
    private MusicEditionAdapterListener listener;

    public MusicEditionAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<Music> objects) {
        super(context, resource, objects);
        musics = objects;
    }

    public void setListener(MusicEditionAdapterListener listener) {
        this.listener = listener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.music_edition_item, null);
        }

        TextView title = (TextView)convertView.findViewById(R.id.txtName);
        ImageView cover = (ImageView)convertView.findViewById(R.id.cover);
        Button delete = (Button)convertView.findViewById(R.id.btnDelete);
        Button edit = (Button)convertView.findViewById(R.id.btnEdit);

        title.setText(musics.get(position).getTitle());
        Picasso.with(getContext()).load(musics.get(position).getCover()).resize(200,200).centerCrop().into(cover);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onEditMusic(musics.get(position));
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null)
                    listener.onDeleteMusic(musics.get(position));
            }
        });

        return convertView;
    }
}