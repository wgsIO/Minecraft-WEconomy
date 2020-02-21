/*
 * Decompiled with CFR 0_132.
 */
package dev.walk.economy.Util;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Cooldown {
    private String Format(long value) {
        if (value > 9L) {
            return "" + value;
        }
        return "0" + value;
    }

    public String getDate() {
        Date date = new Date();SimpleDateFormat formatDay = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat formatHour = new SimpleDateFormat("HH:mm:ss");
        return formatDay.format(date) + " as " + formatHour.format(date);
    }
    public String getDateActual() {
        Date date = new Date();SimpleDateFormat formatDay = new SimpleDateFormat("dd-MM-yyyy"); formatDay.format(date);
        return formatDay.format(date);
    }

    public String getDate(Long time) {
        if (time == 0L) {
            return "0";
        }
        long anos = time / 31104000L;
        time = time - anos * 31104000L;
        long meses = time / 2592000L;
        time = time - meses * 2592000L;
        long dias = time / 86400L;
        time = time - dias * 86400L;
        long horas = time / 3600L;
        time = time - horas * 3600L;
        long minutos = time / 60L;
        time = time - minutos * 60L;
        long segundos = time;
        return this.Format(dias) + "/" + this.Format(meses) + "/" + this.Format(anos) + " \u00e0s " + this.Format(horas) + ":" + this.Format(minutos) + ":" + this.Format(segundos);
    }

    public String getTimeString(long time) {
        if (time == 0L) {
            return "0";
        }
        long anos = time / 31104000L;
        long meses = (time -= anos * 31104000L) / 2592000L;
        long dias = (time -= meses * 2592000L) / 86400L;
        long horas = (time -= dias * 86400L) / 3600L;
        long minutos = (time -= horas * 3600L) / 60L;
        long segundos = time -= minutos * 60L;
        StringBuilder sb = new StringBuilder();
        if (anos > 0L) {
            sb.append(", ").append(anos).append(" ").append(anos == 1L ? "ano" : "anos");
        }
        if (meses > 0L) {
            sb.append(", ").append(meses).append(" ").append(meses == 1L ? "m\u00eas" : "meses");
        }
        if (dias > 0L) {
            sb.append(", ").append(dias).append(" ").append(dias == 1L ? "dia" : "dias");
        }
        if (horas > 0L) {
            sb.append(", ").append(horas).append(" ").append(horas == 1L ? "hora" : "horas");
        }
        if (minutos > 0L) {
            sb.append(",  e ").append(minutos).append(" ").append(minutos == 1L ? "minuto" : "minutos");
        }
        if (segundos > 0L) {
            sb.append(" e ").append(segundos).append(" ").append(segundos == 1L ? "segundo" : "segundos");
        }
        return sb.toString().replaceFirst(", ", "").replaceFirst(" e ", "");
    }

    public long getCurrentTime() {
        try {
            long Seconds = 0L;
            Date now = new Date();
            SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss:dd:MM:yyyy");
            String[] dates = format.format(now).split(":");
            if (Long.parseLong(dates[0]) > 0L) {
                Seconds += 3600L * Long.parseLong(dates[0]);
            }
            if (Long.parseLong(dates[1]) > 0L) {
                Seconds += 60L * Long.parseLong(dates[1]);
            }
            if (Long.parseLong(dates[2]) > 0L) {
                Seconds += Long.parseLong(dates[2]);
            }
            if (Long.parseLong(dates[3]) > 0L) {
                Seconds += 86400L * Long.parseLong(dates[3]);
            }
            if (Long.parseLong(dates[4]) > 0L) {
                Seconds += 2592000L * Long.parseLong(dates[4]);
            }
            if (Long.parseLong(dates[5]) > 0L) {
                Seconds += 31104000L * Long.parseLong(dates[5]);
            }
            return Seconds;
        }
        catch (Exception e) {
            return 0L;
        }
    }

    public long getDateType(long time, TimeType DT) {
        switch (DT) {
            case Ano: {
                return 31104000L * time;
            }
            case Mes: {
                return 2592000L * time;
            }
            case Dia: {
                return 86400L * time;
            }
            case Hora: {
                return 3600L * time;
            }
            case Minuto: {
                return 60L * time;
            }
            case Segundo: {
                return time;
            }
        }
        return time;
    }

    public long[] getTimeInt(long time) {
        if (time == 0L) {
            return new long[]{0, 0, 0, 0, 0, 0};
        }
        long anos = time / 31104000L;
        long meses = (time -= anos * 31104000L) / 2592000L;
        long dias = (time -= meses * 2592000L) / 86400L;
        long horas = (time -= dias * 86400L) / 3600L;
        long minutos = (time -= horas * 3600L) / 60L;
        long segundos = time -= minutos * 60L;
        return new long[]{anos, meses, dias, horas, minutos, segundos};
    }

    public long[] getTimeInt() {
        long time = this.getCurrentTime();
        if (time == 0L) {
            return new long[]{0, 0, 0, 0, 0, 0};
        }
        long anos = time / 31104000L;
        long meses = (time -= anos * 31104000L) / 2592000L;
        long dias = (time -= meses * 2592000L) / 86400L;
        long horas = (time -= dias * 86400L) / 3600L;
        long minutos = (time -= horas * 3600L) / 60L;
        long segundos = time -= minutos * 60L;
        return new long[]{anos, meses, dias, horas, minutos, segundos};
    }

    public String getTimeStringSimplified() {
        long time = this.getCurrentTime();
        if (time == 0L) {
            return "0";
        }
        long anos = time / 31104000L;
        long meses = (time -= anos * 31104000L) / 2592000L;
        long dias = (time -= meses * 2592000L) / 86400L;
        long horas = (time -= dias * 86400L) / 3600L;
        long minutos = (time -= horas * 3600L) / 60L;
        long segundos = time -= minutos * 60L;
        return "" + anos + ":" + meses + ":" + dias + ":" + horas + ":" + minutos + ":" + segundos;
    }

    public String getTimeStringSimplifiedHourMineteSecond() {
        long time = this.getCurrentTime();
        if (time == 0L) {
            return "0";
        }
        long horas = time / 3600L;
        long minutos = (time -= horas * 3600L) / 60L;
        long segundos = time -= minutos * 60L;
        return "" + horas + ":" + minutos + ":" + segundos;
    }

    public int getTimeInt(int[] i, TimeType tt) {
        try {
            return tt == TimeType.Ano ? i[0] : (tt == TimeType.Mes ? i[1] : (tt == TimeType.Dia ? i[2] : (tt == TimeType.Hora ? i[3] : (tt == TimeType.Minuto ? i[4] : (tt == TimeType.Segundo ? i[5] : 0)))));
        }
        catch (Exception e) {
            return 0;
        }
    }

    public enum TimeType {
        Dia,
        Mes,
        Ano,
        Hora,
        Minuto,
        Segundo;
        

        TimeType() {
        }
    }

}

