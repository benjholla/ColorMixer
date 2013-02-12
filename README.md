Color Mixer
==========

A Java implementation of the Kubelka-Munk Theory of Reflectance for mixing RGB colors.  This implementation uses a simplified version of the Kubelka-Munk model that assumes all colors have the same concentration when blending and that all colors are opaque.

Goal
==========
The ultimate goal of this project is to have a intuitive (subtractive color model) yet accurate color mixing function of the form:

`Color mix(Color a, Color b)`

Usage
==========
The KMColorUtils class contains two mix methods for conveniently mixing java.awt.Color objects.

`public static Color mix(Color colorA, Color colorB);`

`public static Color mix(Color... colors);`

References:
==========
1) P. Kubelka, F. Munk, An article on optics of paint layers, August, 1931. (Translated from German by Steve Westin). http://www.graphics.cornell.edu/~westin/pubs/kubelka.pdf

2) Chet S. Haase and Gary W. Meyer. 1992. Modeling pigmented materials for realistic image synthesis.  ACM Trans. Graph. 11, 4 (October 1992), 305-335. DOI=10.1145/146443.146452 http://doi.acm.org/10.1145/146443.146452

3) Blatner, A.M., Ferwerda, J.A., Darling, B.A. and Bailey, R.J. (2011) TangiPaint: a tangible digital painting system. Proceedings IS&T/SID 19th Color Imaging Conference, 102-107. http://www.cis.rit.edu/people/faculty/ferwerda/publications/2011/blatner11_cic.pdf

4) Verification of the kubelka-munk turbid media theory for artist acrylic paint by M Mohammadi, R Berns, 2004. http://art-si.org/PDFs/Processing/KMreport_10_01.pdf

License:
==========
Released under the MIT License (MIT)
