package com.davidalberici.cm_challenge;

import com.davidalberici.cm_challenge.adapter.cmmegaversereader.CmMegaverseReaderRepository;
import com.davidalberici.cm_challenge.adapter.cmmegaversewriter.CmMegaverseWriterRepository;
import com.davidalberici.cm_challenge.adapter.javahttpclient.JavaBuiltInHttpClient;
import com.davidalberici.cm_challenge.hexagon.HexagonApi;
import com.davidalberici.cm_challenge.hexagon.MegaverseChangeDetector;
import com.davidalberici.cm_challenge.hexagon.MegaverseChangeExecutor;
import com.davidalberici.cm_challenge.port.HttpClient;
import com.davidalberici.cm_challenge.port.MegaverseReaderRepository;
import com.davidalberici.cm_challenge.port.MegaverseWriterRepository;

/**
 * Very simple DI mechanism. Fields are public instead of private just to reduce boilerplate to the minimum
 */
public abstract class SimpleDependencyInjection {
    public static final HttpClient httpClient = new JavaBuiltInHttpClient();
    public static MegaverseReaderRepository megaverseReaderRepository =
            new CmMegaverseReaderRepository(httpClient, "b2f8b8be-5953-4d4a-a43d-6752db2b7088");

    public static MegaverseWriterRepository megaverseWriterRepository =
            new CmMegaverseWriterRepository(httpClient, "b2f8b8be-5953-4d4a-a43d-6752db2b7088");

    public static MegaverseChangeDetector megaverseChangeDetector =
            new MegaverseChangeDetector(megaverseWriterRepository);
    public static MegaverseChangeExecutor megaverseChangeExecutor =
            new MegaverseChangeExecutor();
    public static HexagonApi hexagonApi = new HexagonApi(megaverseReaderRepository, megaverseChangeDetector, megaverseChangeExecutor);

}
