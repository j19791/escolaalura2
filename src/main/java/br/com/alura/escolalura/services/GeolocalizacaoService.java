package br.com.alura.escolalura.services;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.stereotype.Service;

import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.GeocodingApiRequest;
import com.google.maps.errors.ApiException;
import com.google.maps.model.GeocodingResult;
import com.google.maps.model.Geometry;
import com.google.maps.model.LatLng;

import br.com.alura.escolalura.models.Contato;

@Service
public class GeolocalizacaoService {

	
	public List<Double> obterLatELongPor(Contato contato) throws ApiException, InterruptedException, IOException{
	    GeoApiContext context = new GeoApiContext().setApiKey("AIzaSyAUsG4wIFULAgdjcXxlvZuvkIu-EGxJOto");//passar apiKey obtida a partir da sua conta 
	    GeocodingApiRequest request = GeocodingApi.newRequest(context).address(contato.getEndereco());//indicar um endereço pelo qual queremos fazer uma pesquisa.

	    GeocodingResult[] results = request.await();//essa operação pode não ser instantânea: aguardar pela resposta. 
	    GeocodingResult resultado = results[0];//selecionar a primeira resposta

	    Geometry geometry = resultado.geometry;
	    LatLng location = geometry.location;

	    return Arrays.asList(location.lat, location.lng);
	  }
}
