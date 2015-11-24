SUMMARY = "Freetype font rendering library"
DESCRIPTION = "FreeType is a software font engine that is designed to be small, efficient, \
highly customizable, and portable while capable of producing high-quality output (glyph \
images). It can be used in graphics libraries, display servers, font conversion tools, text \
image generation tools, and many other products as well."
HOMEPAGE = "http://www.freetype.org/"
BUGTRACKER = "https://savannah.nongnu.org/bugs/?group=freetype"

LICENSE = "FreeType | GPLv2+"
LIC_FILES_CHKSUM = "file://docs/LICENSE.TXT;md5=c017ff17fc6f0794adf93db5559ccd56 \
                    file://docs/FTL.TXT;md5=d479e83797f699fe873b38dadd0fcd4c \
                    file://docs/GPLv2.TXT;md5=8ef380476f642c20ebf40fecb0add2ec"

SECTION = "libs"

SRC_URI = "${SOURCEFORGE_MIRROR}/freetype/freetype-${PV}.tar.bz2 \
           file://0001-sfnt-Fix-Savannah-bug-43680.patch \
           file://0001-truetype-Fix-Savannah-bug-43679.patch \
           file://0001-sfnt-Fix-Savannah-bug-43672.patch \
           file://0001-cff-Fix-Savannah-bug-43661.patch \
           file://0001-bdf-Fix-Savannah-bug-43660.patch \
           file://0001-type42-Fix-Savannah-bug-43659.patch \
           file://0001-type42-Allow-only-embedded-TrueType-fonts.patch \
           file://0001-cff-Fix-Savannah-bug-43658.patch \
           file://0001-sfnt-Fix-Savannah-bug-43656.patch \
           file://0001-type1-type42-Another-fix-for-Savannah-bug-43655.patch \
           file://0001-type1-type42-Fix-Savannah-bug-43655.patch \
           file://0001-Change-some-fields-in-FT_Bitmap-to-unsigned-type.patch \
           file://0001-sfnt-Fix-Savannah-bug-43597.patch \
           file://0001-sfnt-Fix-Savannah-bug-43591.patch \
           file://0001-sfnt-Fix-Savannah-bug-43590.patch \
           file://0001-sfnt-Fix-Savannah-bug-43589.patch \
           file://0001-sfnt-Fix-Savannah-bug-43588.patch \
           file://0001-Fix-Savannah-bug-43548.patch \
           file://0001-Fix-Savannah-bug-43547.patch \
           file://0001-Fix-Savannah-bug-43540.patch \
           file://0001-src-base-ftobjs.c-Mac_Read_POST_Resource-Avoid-memor.patch \
           file://0001-Fix-Savannah-bug-43539.patch \
           file://0001-Fix-Savannah-bug-43538.patch \
           file://0001-src-base-ftobjs.c-Mac_Read_POST_Resource-Use-unsigne.patch \
           file://0001-src-base-ftobjs.c-Mac_Read_POST_Resource-Insert-comm.patch \
           file://0001-src-base-ftobj.c-Mac_Read_POST_Resource-Additional.patch \
           file://0001-Fix-Savannah-bug-43535.patch \
"
SRC_URI[md5sum] = "d6b60f06bfc046e43ab2a6cbfd171d65"
SRC_URI[sha256sum] = "c0848b29d52ef3ca27ad92e08351f023c5e24ce8cea7d8fe69fc96358e65f75e"

BINCONFIG = "${bindir}/freetype-config"

inherit autotools-brokensep pkgconfig binconfig-disabled multilib_header

LIBTOOL = "${S}/builds/unix/${HOST_SYS}-libtool"
EXTRA_OEMAKE = "'LIBTOOL=${LIBTOOL}'"
EXTRA_OEMAKE_class-native = ""
EXTRA_OECONF = "--without-zlib --without-bzip2 CC_BUILD='${BUILD_CC}'"
TARGET_CPPFLAGS += "-D_FILE_OFFSET_BITS=64"


PACKAGECONFIG ??= ""
PACKAGECONFIG[pixmap] = "--with-png,--without-png,libpng"
# This results in a circular dependency so enabling is non-trivial
PACKAGECONFIG[harfbuzz] = "--with-harfbuzz,--without-harfbuzz,harfbuzz"

do_configure() {
	cd builds/unix
	libtoolize --force --copy
	aclocal -I .
	gnu-configize --force
	autoconf
	cd ${S}
	oe_runconf
}

do_configure_class-native() {
	(cd builds/unix && gnu-configize) || die "failure running gnu-configize"
	oe_runconf
}

do_compile_prepend() {
	${BUILD_CC} -o objs/apinames src/tools/apinames.c
}

do_install_append() {
	oe_multilib_header freetype2/config/ftconfig.h
}

BBCLASSEXTEND = "native"
