package pl.musicplayer.repositories;

import pl.musicplayer.models.Song;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Naming mp3 files
 * All files should be named like band_title.mp3. If title contains whitespaces use band_long-title.mp3 convention.
 *
 * Storing mp3 files
 * All files should be stored inside /storage/emulated/0/Music/AndroidMusicPlayer/ folder.
 */
public class SongRepository {
    public static List<Song> songs = getAllSongs();
    public static int currentSongId = 0;

    private static List<Song> getAllSongs() {
        File file = new File("/storage/emulated/0/Music/AndroidMusicPlayer/");
        File[] files = file.listFiles();
        assert files != null;
        List<Song> res = new ArrayList<>();
        for (int i = 0; i < files.length; i++) {
            res.add(new Song(files[i].getName(), i));
        }
        return res;
    }

    public static Song getById(int id) {
        for(Song song : songs) {
            if(song.getId() == id) {
                return song;
            }
        }
        throw new Error("Could not find song");
    }

    public static List<Song> searchByTitle(String titlePhrase) {
        List<Song> result = new ArrayList<>();
        for(Song song : songs) {
            if(song.getTitle().contains(titlePhrase)) {
                result.add(song);
            }
        }
        return result;
    }

    public static Song getNextSong() {
        currentSongId++;
        if (currentSongId >= songs.size())
            currentSongId = 0;

        return songs.get(currentSongId);
    }

    public static Song getPreviousSong() {
        currentSongId--;
        if (currentSongId < 0)
            currentSongId = songs.size() - 1;

        return songs.get(currentSongId);
    }
}
