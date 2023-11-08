package org.example.chess.service.impl;

import lombok.RequiredArgsConstructor;
import org.example.chess.repository.TempRepository;
import org.example.chess.service.TempService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TempServiceImpl implements TempService {
    private final TempRepository tempRepository;
}
