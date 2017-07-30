package com.themagicofmusic.Algorithms;

import com.themagicofmusic.model.MusicFeature;

import org.jaudiotagger.audio.AudioFile;
import org.jaudiotagger.audio.AudioFileIO;
import org.jaudiotagger.audio.AudioHeader;
import org.jaudiotagger.audio.exceptions.CannotReadException;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3AudioHeader;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldKey;
import org.jaudiotagger.tag.Tag;
import org.jaudiotagger.tag.TagException;

import java.io.*;

/**
 * Created by Atharva on 5/29/2017.
 */

public class MusicExtractor {
    public int DEFAULT_NUMBER_COMPONENTS = 3; //default number of components to use for the gmm
    public int skipIntroSeconds = 30;       //number of seconds to skip at the beginning of the song
    public int skipFinalSeconds = 30;       //number of seconds to skip at the end of the song
    public int minimumStreamLength = 30;    //minimal number of seconds of audio data to return a vaild result

    public static MusicFeature ExtractMusicInfo(String fileName)
    {
        AudioFile audioFile =null;
        AudioHeader audioHeader = null;
        File file = new File(fileName);
        MusicFeature musicFeature = new MusicFeature();


        try {
            audioFile = AudioFileIO.read(file);

        } catch (CannotReadException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TagException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ReadOnlyFileException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InvalidAudioFrameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        MP3File f =null;
        try {
            f = (MP3File)AudioFileIO.read(file);
        } catch (CannotReadException | IOException | TagException | ReadOnlyFileException
                | InvalidAudioFrameException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        MP3AudioHeader audioHeader1 = (MP3AudioHeader) f.getAudioHeader();
        audioHeader1.getTrackLength();
        int sampleRate = audioHeader1.getSampleRateAsNumber();
        audioHeader1.getChannels();
        audioHeader1.isVariableBitRate();
        long bitRate = audioHeader1.getBitRateAsNumber();
        Tag tag = audioFile.getTag();
        audioHeader = audioFile.getAudioHeader();
        String tempo= tag.getFirst(FieldKey.TEMPO);
        musicFeature.setBeatTypeCode(tempo);
        return musicFeature;
    }
}
