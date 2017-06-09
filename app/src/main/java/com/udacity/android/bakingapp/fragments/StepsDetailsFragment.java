package com.udacity.android.bakingapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.udacity.android.bakingapp.R;
import com.udacity.android.bakingapp.model.Recipe;
import com.udacity.android.bakingapp.model.Step;

import static com.udacity.android.bakingapp.MainActivity.KEY_RECIPE_ITEM;

/**
 * Created by Muhammad on 5/20/2017
 */

public class StepsDetailsFragment extends Fragment implements ExoPlayer.EventListener {

    private static final String TAG = StepsDetailsFragment.class.getSimpleName();

    private SimpleExoPlayer exoPlayer;
    private SimpleExoPlayerView exoPlayerView;
    private MediaSessionCompat mediaSession;
    private PlaybackStateCompat.Builder stateBuilder;

    private long position = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_steps_details, container, false);

        Recipe recipeItem = (Recipe) getArguments().getSerializable(KEY_RECIPE_ITEM);
        int index = getArguments().getInt("index", 0);
        Step stepItem = null;
        if (recipeItem != null) {
            stepItem = recipeItem.getSteps().get(index);
        }

        exoPlayerView = (SimpleExoPlayerView) view.findViewById(R.id.playerView);
        TextView textViewName = (TextView) view.findViewById(R.id.textViewName);
        TextView textViewDescription = (TextView) view.findViewById(R.id.textViewDescription);

        if (stepItem != null) {
            textViewName.setText(stepItem.getShortDescription());
            textViewDescription.setText(stepItem.getDescription());
            initializeMediaSession();
            initializePlayer(Uri.parse(stepItem.getVideoURL()));
        }

        return view;
    }


    private void initializeMediaSession() {
        mediaSession = new MediaSessionCompat(getContext(), TAG);
        mediaSession.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mediaSession.setMediaButtonReceiver(null);
        stateBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);
        mediaSession.setPlaybackState(stateBuilder.build());
        mediaSession.setCallback(new MySessionCallback());
        mediaSession.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (exoPlayer == null) {
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            exoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            exoPlayerView.setPlayer(exoPlayer);
            exoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(getContext(), "StepsDetailsFragment");
            MediaSource mediaSource = new ExtractorMediaSource(mediaUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            exoPlayer.prepare(mediaSource);
            restExoPlayer(position, false);
        }
    }


    private void restExoPlayer(long position, boolean playWhenReady) {
        this.position = position;
        exoPlayer.seekTo(position);
        exoPlayer.setPlayWhenReady(playWhenReady);
    }

    private void releasePlayer() {
        exoPlayer.stop();
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    public void onPause() {
        super.onPause();
        exoPlayer.setPlayWhenReady(false);
        mediaSession.setActive(false);
    }

    @Override
    public void onResume() {
        super.onResume();
        mediaSession.setActive(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
        mediaSession.setActive(false);
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == PlaybackStateCompat.STATE_PLAYING) {
            position = exoPlayer.getCurrentPosition();
        }
        mediaSession.setPlaybackState(stateBuilder.build());
    }

    private class MySessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            exoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            exoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            restExoPlayer(0, false);
        }
    }

    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {
    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {
    }

    @Override
    public void onLoadingChanged(boolean isLoading) {
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
    }

    @Override
    public void onPositionDiscontinuity() {
    }
}
