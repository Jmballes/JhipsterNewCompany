import { Injectable } from '@angular/core';
import { Http, Response } from '@angular/http';
import { Observable } from 'rxjs/Rx';
import { SERVER_API_URL } from '../../app.constants';
import { User, UserService } from '../../shared';
import { Points } from './points.model';
import { ResponseWrapper, createRequestOption } from '../../shared';

@Injectable()
export class PointsService {
    users: User[];
    private resourceUrl = SERVER_API_URL + 'api/points';
    private resourceSearchUrl = SERVER_API_URL + 'api/_search/points';

    constructor(private http: Http, private userService: UserService) { }

    create(points: Points): Observable<Points> {
        const copy = this.convert(points);
        return this.http.post(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    update(points: Points): Observable<Points> {
        const copy = this.convert(points);
        return this.http.put(this.resourceUrl, copy).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    find(id: number): Observable<Points> {
        return this.http.get(`${this.resourceUrl}/${id}`).map((res: Response) => {
            const jsonResponse = res.json();
            return this.convertItemFromServer(jsonResponse);
        });
    }

    query(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceUrl, options)
            .map((res: Response) => this.convertResponse(res));
    }

    delete(id: number): Observable<Response> {
        return this.http.delete(`${this.resourceUrl}/${id}`);
    }

    search(req?: any): Observable<ResponseWrapper> {
        const options = createRequestOption(req);
        return this.http.get(this.resourceSearchUrl, options)
            .map((res: any) => this.convertResponse(res));
    }
    thisWeek(): Observable<ResponseWrapper> {
          return this.http.get('api/points-this-week')
               .map((res: any) => this.convertResponseWeek(res));
    }
    private convertResponseWeek(res: Response): ResponseWrapper {
        const jsonResponse = res.json();
        const result = [];
        console.log(jsonResponse);
        console.log('Entrando en convertResponseWeek');
        console.log(jsonResponse.length);
        for (let i = 0; i < jsonResponse.length; i++) {
            const user = this.userService.find('1');
            console.log('El tipo de usuario es:');
            console.log(typeof user);
            // result.push(new Points(1,user,3));
        }
        const user2 = this.userService.find('1');
        console.log('El tipo de usuario es:');
        console.log(typeof user2);
        result.push(new Points(1, null, null));
        return new ResponseWrapper(res.headers, result, res.status);
    }
    private convertResponse(res: Response): ResponseWrapper {

        const jsonResponse = res.json();
        console.log('inicio jsonResponse:');
        console.log(jsonResponse);
        console.log('fin jsonResponse:');
        const result = [];
        console.log(jsonResponse.length);
        for (let i = 0; i < jsonResponse.length; i++) {
            console.log('inicio jsonResponse[i]:');
            console.log(jsonResponse[i]);
            console.log('fin jsonResponse[i]:');
            result.push(this.convertItemFromServer(jsonResponse[i]));
        }
        console.log('inicio result:');
        console.log(result);
        console.log('fin result:');
        return new ResponseWrapper(res.headers, result, res.status);
    }

    /**
     * Convert a returned JSON object to Points.
     */
    private convertItemFromServer(json: any): Points {
        console.log('inicio convertItemFromServer:');
        console.log(json);
        console.log('fin convertItemFromServer:');
        const entity: Points = Object.assign(new Points(), json);
        return entity;
    }

    /**
     * Convert a Points to a JSON which can be sent to the server.
     */
    private convert(points: Points): Points {
        const copy: Points = Object.assign({}, points);
        return copy;
    }
}
