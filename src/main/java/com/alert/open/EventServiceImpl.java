package com.alert.open;

import com.alert.open.entity.Event;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements CurdService<Event, String> {

    private final EventRepository eventRepository;

    @Override
    public Optional<Event> getById(String key) {
        return eventRepository.findById(Long.valueOf(key));
    }
}
