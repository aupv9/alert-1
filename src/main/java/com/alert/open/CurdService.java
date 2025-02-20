package com.alert.open;

import java.util.Optional;

public interface CurdService<T, P>{

    Optional<T> getById(P key);
}
