package com.definiteplans.dao;


import java.util.List;

import com.definiteplans.dom.ZipCode;

public interface SearchZipCodesRepository {

    List<ZipCode> getZipCodesByRadius(ZipCode zip, int radius);
}
