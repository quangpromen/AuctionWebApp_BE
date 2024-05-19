package vn.webapp.backend.auction.service;

import vn.webapp.backend.auction.model.Jewelry;

import java.util.List;

public interface IJewelryService {
    List<Jewelry> getAll();
    Jewelry getJewelryById(Integer id);
    List<Jewelry> getJewelryByUsername(String username);
    void deleteJewelry(Integer id);
}
