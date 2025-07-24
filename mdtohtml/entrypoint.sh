#!/bin/sh

mkdir -p /output/metaData/diagrams
cp /input/metaData/diagrams/*.svg /output/metaData/diagrams

pandoc /input/index.md -o /output/index.html
