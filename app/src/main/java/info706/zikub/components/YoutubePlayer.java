package info706.zikub.components;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.util.Log;
import android.util.SparseArray;

import at.huber.youtubeExtractor.VideoMeta;
import at.huber.youtubeExtractor.YouTubeExtractor;
import at.huber.youtubeExtractor.YtFile;

public class YoutubePlayer {
    private MediaPlayer mediaPlayer;
    private Context context;

    public YoutubePlayer(Context context) {
        this.context = context;
        this.mediaPlayer = new MediaPlayer();
    }

    public void start(String videoUrl) {
        new YouTubeExtractor(context) {
            @Override
            public void onExtractionComplete(SparseArray<YtFile> ytFiles, VideoMeta vMeta) {
                if (ytFiles != null) {
                    // Itags list : http://www.genyoutube.net/formats-resolution-youtube-videos.html
                    int itag = 140;
                    String audioUrl = ytFiles.get(itag).getUrl();

                    /*
                    // Parcours des ressources disponibles pour la vidéo Youtube.
                    for(int i=0; i<ytFiles.size(); ++i) {
                        int key = ytFiles.keyAt(i);
                    }
                    */

                    Log.i("AUDIO URL", audioUrl);

                    new YoutubePlayerAsync(mediaPlayer).execute(audioUrl);
                }
            }
        }.extract(videoUrl, true, true);
    }

    /**
     * Called to stop the music (if the medio player is playing).
     */
    public void pause() {
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }

    /**
     * Called to play the music (if the media player is ever prepared).
     */
    public void play() {
        if(!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
        }
    }
}

class YoutubePlayerAsync extends AsyncTask<String, Void, Boolean> {
    private MediaPlayer mediaPlayer;

    public YoutubePlayerAsync(MediaPlayer mediaPlayer) {
        this.mediaPlayer = mediaPlayer;
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(strings[0]);
            mediaPlayer.prepare();

            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.stop();
                    mediaPlayer.reset();
                }
            });

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        Log.i("YOUTUBE PLAYER", "Buffering");
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        mediaPlayer.start();
        Log.i("YOUTUBE PLAYER", "Music playing...");
    }
}
