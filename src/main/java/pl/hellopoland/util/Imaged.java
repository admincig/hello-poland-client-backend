package pl.hellopoland.util;

import pl.hellopoland.bo.ImageCollector;

import java.util.List;

public interface Imaged {

  ImageCollector getMainImage();

  void setMainImage(ImageCollector image);

  List<ImageCollector> getImages();

  void setImages(List<ImageCollector> images);

}
