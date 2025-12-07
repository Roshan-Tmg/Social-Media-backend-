package com.roshan.MyCircle.service;

import com.roshan.MyCircle.exception.TwitException;
import com.roshan.MyCircle.exception.UserException;
import com.roshan.MyCircle.model.Twit;
import com.roshan.MyCircle.model.User;
import com.roshan.MyCircle.repository.TwitRepository;
import com.roshan.MyCircle.request.TwitReplyRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
public class TwitServiceImplementation implements TwitService{
    @Autowired
    private TwitRepository twitRepository;
    @Override
    public Twit createTwit(Twit req, User user) throws UserException {
        Twit twit = new Twit();
        twit.setContent(req.getContent());
        twit.setCreatedAt(LocalDateTime.now());
        twit.setImage(req.getImage());
        twit.setUser(user);
        twit.setReply(false);
        twit.setTwit(true);
        twit.setVideo(req.getVideo());


        return twitRepository.save(twit);
    }

    @Override
    public List<Twit> findAllTwit() {

      // return twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc();

        List<Twit> twits = twitRepository.findAllByIsTwitTrueOrderByCreatedAtDesc();

        LocalDateTime now = LocalDateTime.now();

        for (Twit t : twits) {
            int likeScore = t.getLikes().size();
            int commentScore = t.getReplyTwits().size() * 2;
            long hoursOld = ChronoUnit.HOURS.between(t.getCreatedAt(), now);

            double recencyFactor = Math.max(0.1, 48.0 / (hoursOld + 1));
            t.setFeedScore((int)((likeScore + commentScore) * recencyFactor));
        }

        twits.sort((a, b) -> Integer.compare(b.getFeedScore(), a.getFeedScore()));

        return twits;


    }

    @Override
    public Twit retwit(Long twitId, User user) throws UserException, TwitException {
        Twit twit = findById(twitId);
        if (twit.getRetwitsUser().contains(user)) {
            twit.getRetwitsUser().remove(user);

        }
        else {
            twit.getRetwitsUser().add(user);
        }

        return twitRepository.save(twit);
    }

    @Override
    public Twit findById(Long TwitId) throws TwitException {
        Twit twit = twitRepository.findById(TwitId)
                .orElseThrow(()-> new TwitException("Twit not found with id "+ TwitId));
        return twit;
    }



    @Override
    public void deleteTwitById(Long twitID, Long userId) throws TwitException, UserException {
        Twit twit = findById(twitID);
         if (!userId.equals(twit.getUser().getId())){
             throw new UserException("you cannot delete another user's tweet ");

         }
         twitRepository.deleteById(twit.getId());

    }

    @Override
    public Twit removeFromRetwit(Long twitId, User user) throws TwitException, UserException {
        return null;
    }

    private final RestTemplate restTemplate;

    // Constructor injection (preferred)
    public TwitServiceImplementation(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }
    @Override
    public Twit createdReply(TwitReplyRequest req, User user) throws TwitException, UserException {

        // --- Step 1: Call Python FastAPI for text filtering ---
        String fastApiUrl = "http://localhost:8000/filter";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("text", req.getContent());

        Map<String, String> response = restTemplate.postForObject(fastApiUrl, requestBody, Map.class);

        // Extract cleaned text
        String cleanedContent = response.get("cleaned");


        Twit replyFor = findById(req.getTwitId());

        Twit twit = new Twit();
        twit.setContent(cleanedContent);
        twit.setCreatedAt(LocalDateTime.now());
        twit.setImage(req.getImage());
        twit.setUser(user);
        twit.setReply(true);
        twit.setTwit(false);
        twit.setReplyFor(replyFor);

        Twit saveReply = twitRepository.save(twit);

        replyFor.getReplyTwits().add(saveReply);
        twitRepository.save(replyFor);

        return replyFor;
    }

    @Override
    public List<Twit> getUserTwit(User user) {
        return twitRepository.findByLikesUser_id(user.getId());
    }

    @Override
    public List<Twit> findByLikesContainsUser(User user) {
        return List.of();
    }
}
