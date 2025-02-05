// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//      http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.android.datatransport.cct;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.datatransport.runtime.Destination;
import java.nio.charset.Charset;
import java.util.regex.Pattern;

public final class CCTDestination implements Destination {
  static final String DESTINATION_NAME = "cct";

  static final String DEFAULT_END_POINT =
      StringMerger.mergeStrings("hts/frbslgiggolai.o/0clgbth", "tp:/ieaeogn.ogepscmvc/o/ac");
  static final String LEGACY_END_POINT =
      StringMerger.mergeStrings(
          "hts/frbslgigp.ogepscmv/ieo/eaybtho", "tp:/ieaeogn-agolai.o/1frlglgc/aclg");

  private static final String DEFAULT_API_KEY =
      StringMerger.mergeStrings("AzSCki82AwsLzKd5O8zo", "IayckHiZRO1EFl1aGoK");
  private static final String EXTRAS_VERSION_MARKER = "1$";
  private static final String EXTRAS_DELIMITER = "\\";

  public static final CCTDestination INSTANCE = new CCTDestination(DEFAULT_END_POINT, null);
  public static final CCTDestination LEGACY_INSTANCE =
      new CCTDestination(LEGACY_END_POINT, DEFAULT_API_KEY);

  @NonNull private final String endPoint;
  @Nullable private final String apiKey;

  public CCTDestination(@NonNull String endPoint, @Nullable String apiKey) {
    this.endPoint = endPoint;
    this.apiKey = apiKey;
  }

  @NonNull
  @Override
  public String getName() {
    return DESTINATION_NAME;
  }

  @Nullable
  @Override
  public byte[] getExtras() {
    return asByteArray();
  }

  @Nullable
  public String getAPIKey() {
    return apiKey;
  }

  @NonNull
  public String getEndPoint() {
    return endPoint;
  }

  /**
   * Encodes the current object as a byte[].
   *
   * <p><strong>Important:</strong> Changes in the encoding should be backwards compatible to ensure
   * any data persisted from a previous version can be read successfully.
   */
  @Nullable
  public byte[] asByteArray() {
    if (apiKey == null && endPoint == null) {
      return null;
    }
    String buffer =
        String.format(
            "%s%s%s%s",
            EXTRAS_VERSION_MARKER, endPoint, EXTRAS_DELIMITER, apiKey == null ? "" : apiKey);
    return buffer.getBytes(Charset.forName("UTF-8"));
  }

  /**
   * Creates a new object by parsing a {@code byte[]} generated by {@link asByteArray}.
   *
   * <p><strong>Important:</strong> Changes in the encoding should be backwards compatible to ensure
   * any data persisted from a previous version can be read successfully.
   */
  @NonNull
  public static CCTDestination fromByteArray(@NonNull byte[] a) {
    String buffer = new String(a, Charset.forName("UTF-8"));
    if (!buffer.startsWith(EXTRAS_VERSION_MARKER)) {
      throw new IllegalArgumentException("Version marker missing from extras");
    }
    buffer = buffer.substring(EXTRAS_VERSION_MARKER.length());
    String[] fields = buffer.split(Pattern.quote(EXTRAS_DELIMITER), 2);
    if (fields.length != 2) {
      throw new IllegalArgumentException("Extra is not a valid encoded LegacyFlgDestination");
    }
    String endPoint = fields[0];
    if (endPoint.isEmpty()) {
      throw new IllegalArgumentException("Missing endpoint in CCTDestination extras");
    }
    String apiKey = fields[1];
    return new CCTDestination(endPoint, apiKey.isEmpty() ? null : apiKey);
  }

  @NonNull
  static byte[] encodeString(@NonNull String s) {
    return s.getBytes(Charset.forName("UTF-8"));
  }

  @NonNull
  static String decodeExtras(@NonNull byte[] a) {
    return new String(a, Charset.forName("UTF-8"));
  }
}
