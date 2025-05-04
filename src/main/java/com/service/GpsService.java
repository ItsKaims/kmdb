package com.example.movies.API.service;

import com.example.movies.API.entity.GpsTraffic;
import com.example.movies.API.repository.GpsTrafficRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Transactional
public class GpsService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GpsService.class);
    private final GpsTrafficRepository gpsRepo;

    public GpsService(GpsTrafficRepository gpsRepo) {
        this.gpsRepo = gpsRepo;
    }

    /**
     * Entry-point: raw TCP buffer containing zero or more "( ... )" packets.
     */
    public void parseAndSave(String raw) {
        LOGGER.debug("[TCP] received chunk: {}", raw);

        // 1) split raw into individual packet strings
        List<String> packets = splitPackets(raw);
        LOGGER.debug("Extracted {} packets", packets.size());

        // 2) attempt to parse & save each one
        for (String pkt : packets) {
            LOGGER.debug("Packet: {}", pkt);
            try {
                GpsTraffic rec = parsePacket(pkt);
                if (rec != null) {
                    gpsRepo.save(rec);
                    LOGGER.info("Saved GPS record: {}", rec);
                }
            } catch (Exception ex) {
                LOGGER.error("Failed to parse/save packet {}", pkt, ex);
            }
        }
    }

    /**
     * Splits a raw TCP chunk like "(pkt1)(pkt2)(pkt3)..." into
     * ["pkt1","pkt2","pkt3",...].
     */
    private static List<String> splitPackets(String raw) {
        String clean = raw.replaceAll("\\p{Cntrl}", "");
        List<String> packets = new ArrayList<>();
        Matcher m = Pattern.compile("\\(([^)]*)\\)").matcher(clean);
        while (m.find()) {
            packets.add(m.group(1).trim());
        }
        return packets;
    }

    /**
     * Parse one GTO2 packet of the form
     *   IMEI…BR…LatLonCourseSpeed…L…
     * into a GpsTraffic, or return null to skip.
     */
    private GpsTraffic parsePacket(String pkt) {
        // only handle “BR” + “A” fix packets
        if (!pkt.contains("BR") || !pkt.contains("A")) {
            LOGGER.debug("Skipping non-fix packet");
            return null;
        }

        // IMEI is first 12 chars
        String imei = pkt.substring(0, 12);

        // latitude = four digits + dot + decimals + N/S
        Matcher latM = Pattern.compile("(\\d{4}\\.\\d+)([NS])").matcher(pkt);
        if (!latM.find()) {
            LOGGER.warn("No latitude in {}", pkt);
            return null;
        }
        double lat = convertToDecimal(latM.group(1), latM.group(2).charAt(0));

        // longitude = five digits + dot + decimals + E/W
        Matcher lonM = Pattern.compile("(\\d{5}\\.\\d+)([EW])").matcher(pkt);
        if (!lonM.find(latM.end())) {
            LOGGER.warn("No longitude in {}", pkt);
            return null;
        }
        double lon = convertToDecimal(lonM.group(1), lonM.group(2).charAt(0));

        // course & speed come immediately before the “L” terminator,
        // in a single run of digits+dots with no comma.
        // We split that into two halves: the first 3+ after the decimal
        // is course, the remainder is speed.
        String tail = pkt.substring(lonM.end());
        Matcher csM = Pattern.compile("(\\d{3}\\.\\d+)(\\d+\\.\\d+)L").matcher(tail);
        if (!csM.find()) {
            LOGGER.warn("No course/speed in {}", pkt);
            return null;
        }
        double course = Double.parseDouble(csM.group(1));
        double speed  = Double.parseDouble(csM.group(2));

        // use server receive time as timestamp
        LocalDateTime ts = LocalDateTime.now();
        LOGGER.debug("Server timestamp: {}", ts);

        return new GpsTraffic(imei, ts, lat, lon, speed, course);
    }

    /**
     * Convert a coordinate from DDMM.MMMM + hemisphere into decimal degrees.
     */
    private double convertToDecimal(String coord, char hemi) {
        double ddmm = Double.parseDouble(coord);
        double deg  = Math.floor(ddmm / 100);
        double min  = ddmm - deg * 100;
        double dec  = deg + (min / 60.0);
        return (hemi == 'S' || hemi == 'W') ? -dec : dec;
    }
}
