package com.pongo.pongoedu.application.port;

import com.pongo.pongoedu.application.dto.request.SugestaoRoteiroRequest;
import com.pongo.pongoedu.application.dto.response.SugestaoRoteiroResponse;
import com.pongo.pongoedu.domain.entity.Produto;

import java.util.List;

public interface AssistenteRoteiroPort {

    SugestaoRoteiroResponse sugerirRoteiro(SugestaoRoteiroRequest pedido, List<Produto> materiaisDisponiveis);
}
