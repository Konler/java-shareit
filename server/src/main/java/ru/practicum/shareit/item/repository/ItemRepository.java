package ru.practicum.shareit.item.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.shareit.exceptions.NotFoundException;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.messages.LogMessages;

public interface ItemRepository extends JpaRepository<Item, Long> {

    default Item validateItem(Long itemId) {
        return findById(itemId).orElseThrow(() -> new NotFoundException(
                LogMessages.NOT_FOUND.toString() + itemId));
    }

    @Query("SELECT i FROM Item AS i " +
            "WHERE UPPER(i.name) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "OR UPPER(i.description) LIKE UPPER(CONCAT('%', ?1, '%')) " +
            "AND i.available IS TRUE")
    Page<Item> searchItemByText(String text, Pageable pageable);

    Page<Item> findAllByOwnerId(Long userId, Pageable pageable);
}