package pl.hellopoland.util;

import pl.hellopoland.bo.ImageCollector;

public interface Imaged {

  ImageCollector getMainImage();

  void setMainImage(ImageCollector image);

}
