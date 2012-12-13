# img-palette

A simple demo of extracting dominant colors from an image using K-means clustering.

## Usage

    (require '[img-palette.core :as ip])

    (ip/img->palette "/path/to/source-file.png" "/path/to/palette-file.png")

## License

Copyright © 2012 Baishampayan Ghose

Distributed under the Eclipse Public License, the same as Clojure.
